# HCI_Sentiment_Analysis_Min_Chen
Sentiment analysis module for intelligent tutoring system.

# Dependency Requirement
1. Java 1.8
2. All the dependencies libraries are under the /lib directory in this repository except for the Stanford core nlp library, which needs to be installed using mvn plugin in Intellij (this will be covered in next section).

# How to Install
1. Download IDE(Intellij Community Version) from https://www.jetbrains.com/idea/download/?utm_expid=.ofjb89XbTQqGQqkIvxouYw.0&utm_referrer=https%3A%2F%2Fwww.google.com%2F#section=mac .
2. Go to your desired local directory.
3. Run command to clone the sentiment analysis repository from github: ```git clone https://github.com/Min-Chen/HCI_Sentiment_Analysis_Min_Chen.git```.
4. Use Intellij to open this repository locally.
5. Install Stanford core nlp library.
   - In Intellij, go to file -> Project Stucture;
   ![alt text](https://cloud.githubusercontent.com/assets/9358694/25786362/819ddda8-3348-11e7-8de3-24d186457cd8.png)
   
   - Install stanford core nlp library
   ![alt text](https://cloud.githubusercontent.com/assets/9358694/25786395/37f13dc0-3349-11e7-9e7b-ddd45a91bd9f.png)
   
   Input the library name and hit enter to search for the library in mvn.
   ![alt text](https://cloud.githubusercontent.com/assets/9358694/25786402/3e96062e-3349-11e7-9a4c-5bd1abeacbf7.png)
   
   Wait for some time to find the library. When the "OK" button is ready, hit "OK".
   ![alt text](https://cloud.githubusercontent.com/assets/9358694/25786405/44f99c42-3349-11e7-8f02-de0ced782d7d.png)
   
   Click OK. And the Stanford core nlp library should be installed successfully.
   ![alt text](https://cloud.githubusercontent.com/assets/9358694/25786408/4db74744-3349-11e7-85c6-e6cd1b22e2cd.png)
   
6. Install Stanford core nlp models.
   - Download CoreNLP models jar file(362.5 MB) from this link: https://www.dropbox.com/s/h2wc4zauzdkovlx/stanford-corenlp-models-current.jar?dl=0
   - Move this stanford-corenlp-models-current.jar to the lib directory of this repository.

# How to use
1. Make sure the repository is installed locally.
2. Run the main class ```DriverSentimentAnalysis.java```. You should see that "> Server starts at port: 3050". 
That means this sentiment analysis module is successfully running.
3. Type the sentence you want to do sentiment analysis.
- e.g. Type in "Japanese is too hard for me to learn." 
The system will do sentiment analysis on the sentence, and output the sentiment result for you below this sentence.
