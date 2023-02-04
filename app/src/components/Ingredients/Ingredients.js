import React, { useState, useEffect, useCallback } from 'react';

import ProductItem from '../Products/ProductItem';
import IngredientForm from './IngredientForm';
import IngredientList from './IngredientList';
import ErrorModal from '../UI/ErrorModal';
import Search from './Search';
import axios from 'axios';
import { DataGrid, GridColDef, gridColumnLookupSelector, GridValueGetterParams } from '@mui/x-data-grid';

const Ingredients = () => {
  const [userIngredients, setUserIngredients] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [selectedRows, setSelectedRows] = useState([])
  const [pdfUrl, setPdfUrl] = useState([])
  const list = []

  useEffect(() => {
    console.log('RENDERING INGREDIENTS', userIngredients);
  }, [userIngredients]);

  const filteredIngredientsHandler = useCallback(filteredIngredients => {
    setUserIngredients(filteredIngredients);
  }, []);

  const addIngredientHandler = ingredient => {
    setIsLoading(true);
    const data = {
      id: selectedRows[0].id,
      amount: selectedRows[0].amount,
      name: selectedRows[0].name,
    };
    console.log('data: ', data)
    const arr = [data]
    axios.post('/pdf', arr)
    .then(response => {
      console.log('response: ', response.data);
      // this.setState( { pdfUrls: [...this.state.pdfUrls, response.data] })
      setPdfUrl(prevUrlList => [
        ...prevUrlList,
        { id: response.data.id, ...response.data }
      ])
      console.log('response: state', pdfUrl);
      list.push(response.data)
      console.log('response: state list', list);
    })
    .catch(error => {
      console.log('error: ', error)
    });
  };

  const removeIngredientHandler = ingredientId => {
    setIsLoading(true);
    fetch(
      `https://react-hooks-update.firebaseio.com/ingredients/${ingredientId}.jon`,
      {
        method: 'DELETE'
      }
    ).then(response => {
      setIsLoading(false);
      setUserIngredients(prevIngredients =>
        prevIngredients.filter(ingredient => ingredient.id !== ingredientId)
      );
    }).catch(error => {
      setError('Something went wrong!');
      setIsLoading(false);
    });
  };

  const clearError = () => {
    setError(null);
  }

  return (
    <div className="App">
      {error && <ErrorModal onClose={clearError}>{error}</ErrorModal>}

      {/* <IngredientForm
        onAddIngredient={addIngredientHandler}
        loading={isLoading}
      />

      <section>
        <Search onLoadIngredients={filteredIngredientsHandler} />
        <IngredientList
          ingredients={userIngredients}
          onRemoveItem={removeIngredientHandler}
        />
      </section> */}
      <DataGrid 
      style={{ height: 400, width: '50%'}}
      rows={rows} 
      columns={columns} 
      checkboxSelection
      disableSelectionOnClick
      onSelectionModelChange={(ids) => {
        const selectedIDs = new Set(ids);
        const selectedRowData = rows.filter((row) =>
          selectedIDs.has(row.id)
        );
        setSelectedRows(selectedRowData);
        // this.setState( { selectedRows: selectedRowData } );
        // console.log(selectedRowData);
        console.log('selectedRows', selectedRows)
      }}
      />
      <button 
        variant="contained"
        onClick={() => {
          addIngredientHandler()
        }}>Contained
      </button>
      <ul className="products-list">
        {pdfUrl.map(prod => (
          <ProductItem
            key={prod.id}
            id={prod.id}
            url={prod.url}
          />
        ))}
      </ul>
    </div>
  );
};
const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 90 },
  {
    field: 'name',
    headerName: 'Name',
    width: 150,
    editable: true,
  },
  {
    field: 'amount',
    headerName: 'Amount',
    type: 'number',
    width: 110,
    editable: true,
  },
 
];

const rows = [
  { id: 1, name: 'Snow', amount: 35 },
  { id: 2, name: 'Lannister', amount: 42 },
  { id: 3, name: 'Lannister', amount: 45 },
  { id: 4, name: 'Stark', amount: 16 },
  { id: 5, name: 'Targaryen', amount: null },
];
export default Ingredients;
