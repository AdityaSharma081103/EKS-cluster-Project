const express = require('express');
const app = express();
const PORT = 3000;

app.get('/', (req, res) => {
  res.send('Welcome to Amazon App!');
});

app.listen(PORT, () => {
  console.log(`Amazon app listening on port ${PORT}`);
});
