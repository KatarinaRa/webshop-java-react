import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import '../ProductList.css';
import authHeader from '../services/auth-header';

function ProductList() {
    const [products, setProducts] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [productCategories, setProductCategories] = useState([]);
    const [colors, setColors] = useState([]);
    const [sizes, setSizes] = useState([]);
    const [genders, setGenders] = useState([]);
    const [orderId, setOrderId] = useState(localStorage.getItem('orderId'));
    const [isModalOpen, setModalOpen] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [totalPages, setTotalPages] = useState(0);
    const [isAdmin, setIsAdmin] = useState(false);
    const [sortType, setSortType] = useState('none');
    
    const [formFields, setFormFields] = useState({
        name: '',
        description: '',
        price: '',
        color: '',
        size: '',
        gender: '',
        imageURL: ''
    });

    

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = products.slice(indexOfFirstItem, indexOfLastItem);

    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(products.length / itemsPerPage); i++) {
        pageNumbers.push(i);
    }

    useEffect(() =>{
        let sortedProducts = [...products];
        if (sortType === 'asc'){
            sortedProducts.sort((a,b) => a.price - b.price)
        }
        else if (sortType === 'desc'){
            sortedProducts.sort((a,b) => b.price - a.price);
        }
        setProducts(sortedProducts);
    }, [sortType]);


    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.roles && user.roles.includes('ROLE_ADMIN')) {
            setIsAdmin(true);
        }
    }, []);

    useEffect(() => {
        fetchProducts();
        fetchProductCategories();
        fetchColors();
        fetchSizes();
        fetchGenders();
    }, []);

    useEffect(() => {
        if (selectedCategory) {
            fetchProductsByCategory(selectedCategory);
        } else {
            fetchProducts();
        }
    }, [selectedCategory]);

    useEffect(() => {
        console.log("isModalOpen changed:", isModalOpen);
    }, [isModalOpen]);

    const fetchProducts = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/products?page=${currentPage - 1}`, { 
                headers: authHeader(),
            });
    
            if (response.status !== 200) {
                console.error("Received unexpected status code:", response.status);
                return;
            }
    
            if (response.data && Array.isArray(response.data.content)) {
                let allGood = true;
                for (const product of response.data.content) {
                    if (!product.id || !product.name) {
                        allGood = false;
                        break;
                    }
                }
    
                if (!allGood) {
                    console.error("Received malformatted product data:", response.data.content);
                    return;
                }
    
                setProducts(response.data.content);
                setTotalPages(response.data.totalPages);  
            } else {
                console.error("API didn't return an array for products:", response.data);
            }
        } catch (error) {
            if (error.response) {
                console.error("Server responded with an error:", error.response.status, error.response.data);
            } else if (error.request) {
                console.error("No response received:", error.request);
            } else {
                console.error("Error setting up the request:", error.message);
            }
        }
    };

    const fetchProductsByCategory = async (category) => {
        const response = await axios.get(`http://localhost:8080/products?category=${category}`, {
            headers: authHeader(),
        });
        setProducts(response.data);
    };

    const fetchProductCategories = async () => {
        const response = await axios.get('http://localhost:8080/productscategory', {
            headers: authHeader(),
        });
        setProductCategories(response.data);
    };

    const fetchColors = async () => {
        const response = await axios.get('http://localhost:8080/colors', {
            headers: authHeader(),
        });
        setColors(response.data);
    };

    const fetchSizes = async () => {
        const response = await axios.get('http://localhost:8080/sizes', {
            headers: authHeader(),
        });
        setSizes(response.data);
    };

    const fetchGenders = async () => {
        const response = await axios.get('http://localhost:8080/genders', {
            headers: authHeader(),
        });
        setGenders(response.data);
    };

    const deleteProduct = async (productId) => {
        await axios.delete('http://localhost:8080/products/${productId}', {
            headers: authHeader(),
        });
        alert('Proizvod uspješno obrisan.');
        fetchProducts();
    };

    const openModal = (product) => {
        console.log("Opening modal for product:", product);
        setEditingProduct(product);
        setFormFields({
            name: product.name,
            description: product.description,
            price: product.price,
            color: product.color?.name,
            size: product.size?.name,
            gender: product.gender?.name,
            imageURL: product.imageURL
        });
        setModalOpen(true);
    };

    const closeModal = () => {
        setModalOpen(false);
        setEditingProduct(null);
        fetchProducts();
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormFields(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    const getColorIdFromName = (name) => {
        const color = colors.find(c => c.name === name);
        return color ? color.id : null;
      };
      
      const getSizeIdFromName = (name) => {
        const size = sizes.find(s => s.name === name);
        return size ? size.id : null;
      };
      
      const getGenderIdFromName = (name) => {
        const gender = genders.find(g => g.name === name);
        return gender ? gender.id : null;
      };

    const handleEditSubmit = async (event) => {
        event.preventDefault();
        console.log('Form fields before update:', formFields);
    
        const colorId = getColorIdFromName(formFields.color);
        const sizeId = getSizeIdFromName(formFields.size);
        const genderId = getGenderIdFromName(formFields.gender);
    
        const updatedProduct = {
            ...editingProduct,
            name: formFields.name,
            description: formFields.description,
            price: formFields.price,
            color: { id: colorId },
            size: { id: sizeId },
            gender: { id: genderId },
            imageURL: formFields.imageURL
        };
    
        try {
            await axios.put(`http://localhost:8080/products/${editingProduct.id}`, updatedProduct);
            console.log('Product updated successfully.');
            await fetchProducts(); 
            closeModal();
        } catch (error) {
            console.error('Error updating product:', error.response ? error.response.data : error);
        }
    };



    return (
        <div>
            <h2>Popis proizvoda</h2>
            <div className='filter-section'>
                <div className="sort-options">
                    <label htmlFor='sortType'>Sortiraj proizvode: </label>
                    <select id='sortType' value={sortType} onChange={(e) => setSortType(e.target.value)}>
                        <option value='none'>Bez sortiranja</option>
                        <option value='asc'>Od najmanje do najveće cijene</option>
                        <option value='desc'>Od najveće do najmanje cijene</option>
                    </select>
                </div>
                <br />
            </div>
            <div className="category-list">
                <button onClick={() => setSelectedCategory('')}>Svi proizvodi</button>
                {productCategories.map((category) => (
                    <button key={category.id} onClick={() => setSelectedCategory(category.name)}>
                        {category.name}
                    </button>
                ))}
            </div>
            <div className="product-list">
                {products.map((product) => (
                    <div key={product.id} className="product-item">
                        <div className="product-image-container">
                            <Link to={`/product/${product.id}`}>
                                <img
                                    src={`http://localhost:8080/products/images/${product.imageURL}`}
                                    alt="Slika proizvoda"
                                    className="product-image"
                                />
                            </Link>
                        </div>
                        <h3>{product.name}</h3>
                        <p>{product.price} €</p>
                        {isAdmin && <button onClick={() => openModal(product)}>Uredi</button>}
                        {isModalOpen && editingProduct === product && (
                            <div className="edit-modal">
                                <form onSubmit={handleEditSubmit}>
                                    <div>
                                        <label>Ime:</label>
                                        <input type="text" name="name" value={formFields.name} onChange={handleInputChange} />
                                    </div>
                                    <div>
                                        <label>Opis:</label>
                                        <textarea name="description" value={formFields.description} onChange={handleInputChange} />
                                    </div>
                                    <div>
                                        <label>Cijena:</label>
                                        <input type="text" name="price" value={formFields.price} onChange={handleInputChange} />
                                    </div>
                                    <div>
                                        <label>Boja:</label>
                                        <select name="color" value={formFields.color} onChange={handleInputChange}>
                                            {colors.map(color => <option key={color.id} value={color.name}>{color.name}</option>)}
                                        </select>
                                    </div>
                                    <div>
                                        <label>Veličina:</label>
                                        <select name="size" value={formFields.size} onChange={handleInputChange}>
                                            {sizes.map(size => <option key={size.id} value={size.name}>{size.name}</option>)}
                                        </select>
                                    </div>
                                    <div>
                                        <label>Spol:</label>
                                        <select name="gender" value={formFields.gender} onChange={handleInputChange}>
                                            {genders.map(gender => <option key={gender.id} value={gender.name}>{gender.name}</option>)}
                                        </select>
                                    </div>
                                    <div>
                                        <label>Slika:</label>
                                        <input type="text" name="imageURL" value={formFields.imageURL} onChange={handleInputChange} />
                                    </div>
                                    <button type="submit">Spremi izmjene</button>
                                    <button onClick={closeModal}>Zatvori</button>
                                </form>
                            </div>
                        )}
                        {isAdmin && <button onClick={() => deleteProduct(product.id)}>Obriši</button>}
                    </div>
                ))}
            </div>
            <div className="pagination">
            {[...Array(totalPages).keys()].map((page, index) => (
            <button key={index} onClick={() => {
            setCurrentPage(page + 1); 
            fetchProducts(); 
         }}>
             {page + 1}
         </button>
         ))}
        </div>
            <Link to="/cart">Prikaži košaricu</Link>
        </div>
    );
    
}

export default ProductList;