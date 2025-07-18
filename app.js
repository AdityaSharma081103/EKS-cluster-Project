const express = require('express');
const app = express();
const PORT = 3000;

app.get('/', (req, res) => {
  res.send('Welcome to Flipkart App!');
});

app.listen(PORT, () => {
  console.log(`Flipkart app listening on port ${PORT}`);
});
