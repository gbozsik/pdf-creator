import React, { useContext } from 'react';

import Card from '../UI/Card';
import './InvoiceItem.css';

const InvoiceItem = props => {

  const baseURL = 'http://localhost:8080/api/pdf/'

  const onButtonClick = () => {
    fetch(baseURL + props.fileName).then(response => {
        response.blob().then(blob => {
            const fileURL = window.URL.createObjectURL(blob);
            let alink = document.createElement('a');
            alink.href = fileURL;
            alink.download = props.fileName
            alink.click();
        })
    })
}

  return (
    <Card style={{ marginBottom: '1rem' }}>
      <div >
        <h2 className='is-fav'>{props.fileName}</h2>
        <button
          onClick={onButtonClick}
        >
          {props.isFav ? 'Un-Favorite' : 'Favorite'}
        </button>
      </div>
    </Card>
  );
};

export default InvoiceItem;
