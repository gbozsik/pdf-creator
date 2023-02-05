import React, { useState, useEffect } from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';

import "./PdfGenerator.css"
import InvoiceItem from '../Invoices/InvoiceItem';
import ErrorModal from '../UI/ErrorModal';
import axios from 'axios';

const PdfGenerator = () => {
  const [error, setError] = useState();
  const [selectedRows, setSelectedRows] = useState([])
  const [rows, setRows] = useState([])
  const [pdfNames, setPdfNames] = useState([])

  useEffect(() => {
    axios.get('/pdf')
    .then(response => {
      if (response.data) {
        setRows(response.data)
      }
    })
  }, [pdfNames]);

  const createReportHandler = () => {
    const arr = selectedRows.map(row => {
      return {
        id: row.id,
        name: row.name,
        amount: row.amount,
      }
    })
    axios.post('/pdf', arr)
    .then(response => {
      setPdfNames(prevUrlList => [
        ...prevUrlList,
        { id: response.data.id, ...response.data }
      ])
    })
    .catch(error => {
      console.log('error: ', error.response.data.code)
      if (error.response.data.code === 'VALIDATION_ERROR') {
        setError(error.response.data.message)
      } else {
        setError('Something went wrong!')
      }
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
          const selectedIDs = new Set(ids);
          const selectedRowData = rows.filter((row) =>
            selectedIDs.has(row.id)
          );
          setSelectedRows(selectedRowData);
        }}/>

      <button 
        variant="contained"
        onClick={() => {
          createReportHandler()
        }}>Generate invoice
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
  {
    field: 'name',
    headerName: 'Name',
    width: 200,
    editable: true,
  },
  {
    field: 'amount',
    headerName: 'Amount',
    type: 'number',
    width: 150,
    editable: true,
  },
 
];
export default PdfGenerator;
