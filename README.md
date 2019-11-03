# Red Cross Crisis Locator: A project for Openhack 2019 by Hack-A-Cross (https://github.com/openhack-hackacross/openhack-sthlm-2019/blob/master/README.md)
This is an entry for the Stockholm Openhack 2019. It is an application for collecting news from specific RSS-feeds and filtering these to map and show news related to certain keywords (fire, accident) in order to assist in emergency administration of volunteers. 

## Goal
The goal is to provide a filtered visualisation of data that can help predict emergency situations for second line help organisations such as the Swedish Red Cross. By combining different data sources, such as news items and wheather conditions, we aim to give a view where administrators can find relevant information to help in assessing if a situation will or has just occured in order to provide faster help. The app is currently localised to Sweden but can be scaled for similar projects elsewhere. 

## Current Status

### What we have
A basic app that pulls RSS-feeds from: the Swedish Police, the Swedish Emergency Service through Helsingborg City and local news in Helsingborg City. It then uses tokenization to filter for keywords in the texts in order to provide and map only the items relevant to the user. The filters currently in place are 'fire' and 'accident'. In the frontend, we have a simple UI that shows a map on which the items are plotted with markers. These can be filtered using the types above. 

### Technical Specifications
Backend is javascript and node.js, and frontend is java. For natural language processing, we use 'natural' for node.js. The natural language processing currently only tokenize the items and performs no actual analysis and categorization. 
Backend can be found in this Repo (https://github.com/openhack-hackacross/openhack-sthlm-2019/blob/master/README.md)

## Roadmap

### Combining Data Sources for Better Predictions
Currently, we only gather a small set of RSS-items that we plot on the map. These would need to be increased to cover local and national news across the country. We would also need more filters to provide relevant sources, for example 'storms', 'riots' or similar. 

Going beyond the RSS, we want to add other types of data to help the administrators improve their predictions of a situation. For example, a heatmap could overlay the map under the 'fire' filter to show the current risk of fire in grass or forest. Combining this with the news items, it could provide a way of seeing increased risks of fire spread. Weather and traffic data could be used in a similar manner. 

### Building on the Natural Language Processing
The natural language processing is not really utilised in this iteration of the app. We only perform tokenization and then use the tokens to filter the items. In the future, we want to classify items using the NLP and use that classification to filter the items instead. This would also improve finding the location of an event, as many RSS-items does not include coordinates. By finding possible place names in the items, we could map them with more precision. 

### Improved performance and code
Several improvements could be made for performande. This includes gathering and cleaning the data and performing the analysis in order to be properly scaleable. It might also be useful to look into NLP in python rather than node.js. 

## How To Run

### Requirements:
- Node.js
- npm

### Instructions:
1. Clone the repository
2. Run ```npm install```
3. If running locally, you'll need to change the port in index.js to the relevant port 
4. Run ```npm start```
