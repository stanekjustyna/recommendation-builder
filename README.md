# recommendation-builder

Recommendation builder provides MovieWorld Web Application with data. It loads data to mySql database.

- Spring Batch module loads data from <a href="https://github.com/sidooms/MovieTweetings" target="_blank">MovieTweetings</a> database.
- Detailed movie information is taken from <a href="https://www.imdb.com" target="_blank">imdb</a> database.
- application uses Apache Mahout framework to produce recommendations for users and items. 
- Recommendation are build based on Collaborative-Filtering algorithm.

