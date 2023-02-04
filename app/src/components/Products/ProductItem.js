import React, { useContext } from 'react';

import Card from '../UI/Card';
// import { ProductsContext } from '../../context/products-context';
import './ProductItem.css';

const ProductItem = props => {

  // const toggleFav = useContext(ProductsContext).toggleFav;

  // const toggleFavHandler = () => {
  //   toggleFav(props.id);
  // };

  const onButtonClick = () => {
    // using Java Script method to get PDF file
    // fetch('../../iTextTable.pdf').then(response => {
    fetch('http://localhost:8080/api/pdf/a.pdf').then(response => {
        response.blob().then(blob => {
            // Creating new object of PDF file
            const fileURL = window.URL.createObjectURL(blob);
            // Setting various property values
            let alink = document.createElement('a');
            alink.href = fileURL;
            alink.download = 'a.pdf';
            alink.click();
        })
    })
}

  return (
    <Card style={{ marginBottom: '1rem' }}>
      <div className="product-item">
        <h2 className={props.isFav ? 'is-fav' : ''}>{props.url}</h2>
        {/* <p>{props.url}</p> */}
        <button
          className={!props.isFav ? 'button-outline' : ''}
          onClick={onButtonClick}
        >
          {props.isFav ? 'Un-Favorite' : 'Favorite'}
        </button>
      </div>
    </Card>
  );
};

export default ProductItem;
