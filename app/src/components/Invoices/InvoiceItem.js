import React, { useState } from 'react';

import Card from '../UI/Card';
import './InvoiceItem.css';
import ErrorModal from '../UI/ErrorModal';

const InvoiceItem = props => {

  const baseURL = 'http://localhost:8080/api/pdf/'

  const [error, setError] = useState();

  const onButtonClick = () => {
    fetch(baseURL + props.fileName).then(response => {
      if (response.status === 201) {
        response.blob().then(blob => {
            const fileURL = window.URL.createObjectURL(blob);
            let alink = document.createElement('a');
            alink.href = fileURL;
            alink.download = props.fileName
            console.log(blob)
            alink.click();
        })
        .catch(error => {
          console.log('error: ', error)
          setError('Something went wrong!')
        });
      } else {
        throw Error()
      }
    }).catch(error => {
      console.log('error: ', error)
      setError('Something went wrong!')
    })
  }

  const clearError = () => {
    setError(null);
  }

  return (
    <div>
      {error && <ErrorModal onClose={clearError}>{error}</ErrorModal>}
      <Card style={{ marginBottom: '1rem' }}>
        <div >
          <h2 className='is-fav'>{props.fileName}</h2>
          <button
            onClick={onButtonClick}
          >
            {'Download invoice'}
          </button>
        </div>
      </Card>
    </div>
  );
};

export default InvoiceItem;
