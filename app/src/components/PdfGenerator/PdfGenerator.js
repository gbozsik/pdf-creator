import React, { useState, useEffect, useCallback } from 'react';

import InvoiceItem from '../Invoices/InvoiceItem';
import "./PdfGenerator.css"
import ErrorModal from '../UI/ErrorModal';
import axios from 'axios';
import { DataGrid, GridColDef } from '@mui/x-data-grid';

const PdfGenerator = () => {
  const [error, setError] = useState();
  const [selectedRows, setSelectedRows] = useState([])
  const [pdfNames, setPdfNames] = useState([])

  useEffect(() => {
    console.log('RENDERING pdfNames', pdfNames);
  }, [pdfNames]);

  useEffect(() => {
    console.log('RENDERING selected rows', selectedRows);
  }, [selectedRows]);

  // const filteredIngredientsHandler = useCallback(filteredIngredients => {
  //   setUserIngredients(filteredIngredients);
  // }, []);

  const addIngredientHandler = ingredient => {
    const arr = selectedRows.map(row => {
      return {
        id: row.id,
        name: row.name,
        amount: row.amount,
      }
    })
    axios.post('/pdf', arr)
    .then(response => {
      console.log('response: ', response.data);
      setPdfNames(prevUrlList => [
        ...prevUrlList,
        { id: response.data.id, ...response.data }
      ])
    })
    .catch(error => {
      console.log('error: ', error)
    });
  };

  const clearError = () => {
    setError(null);
  }

  return (
    <div className="Base"
    >
      {error && <ErrorModal onClose={clearError}>{error}</ErrorModal>}

      <DataGrid 
      rows={rows} 
      columns={columns} 
      checkboxSelection
      disableSelectionOnClick
      onSelectionModelChange={(ids) => {
        console.log('ids: ', ids)
        const selectedIDs = new Set(ids);
        const selectedRowData = rows.filter((row) =>
          selectedIDs.has(row.id)
        );
        setSelectedRows(selectedRowData);
      }}
      />
      <button 
        variant="contained"
        onClick={() => {
          addIngredientHandler()
        }}>Contained
      </button>
      <ul>
        {pdfNames.map(pdfName => (
          <InvoiceItem
            key={pdfName.id}
            id={pdfName.id}
            fileName={pdfName.fileName}
            pdf={pdfNames[pdfName]}
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
export default PdfGenerator;
