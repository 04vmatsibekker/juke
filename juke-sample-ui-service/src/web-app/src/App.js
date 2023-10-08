
import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import Box from '@mui/material/Box';
import AccountCircle from '@mui/icons-material/AccountCircle';
import InputAdornment from '@mui/material/InputAdornment';
import Button from "@mui/material/Button";
import InputLabel from '@mui/material/InputLabel';
import Card from '@mui/material/Card';
import Divider from '@mui/material/Divider';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Input from "@mui/material/Input";
import FormControl from '@mui/material/FormControl';
import { styles } from "./css-common";
import axios from "axios";
import  { useState } from 'react';






const App = () =>{

 const [text, setText] = useState("");
const [response, setResponse] = useState("");

const inputText= (event) =>{
    setText(event.target.value);
}

const getResponse = () =>{
    return this.response;
};
const callHome = () =>{
    axios.get('/greeting'+"?name="+text).then(response => {
    console.log(response.data);
    const result = (response.data && response.data.content) ? response.data.content : "";
    setResponse(result);
  }, error => {
    console.log(error);
  }
)
};
  return (
    <div >
        <Card variant="outlined">
            <CardContent>
                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom> UI Example
                     </Typography>
<br></br>

        <Box sx={{ display: 'inline', flexWrap: 'wrap' }}>
         <FormControl variant="standard">

                <InputLabel htmlFor="input-with-icon-adornment">
                      Add a Name
                    </InputLabel>
               <Input id="inputName" onChange={inputText}
                    variant="filled"
                     startAdornment={
                       <InputAdornment position="start">
                         <AccountCircle />
                       </InputAdornment>
                     }
                   />
                   <br></br>
                   <Button variant="contained" onClick={callHome}>Submit</Button>
                    <br></br>
                    <b>Response: {response}</b>
          </FormControl>
         </Box>

            </CardContent>
        </Card>


    </div>
  );
}

export default App;
