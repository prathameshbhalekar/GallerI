from scraping.items import imageItem
import scrapy
import os

class unsplash_spider(scrapy.Spider):
    name = "unsplash_spider"

    def __init__(self):
        categories = [
        'nature',
        'people',
        'architecture',
        'fashion',
        'health',
        'interiors',
        'street-photography',
        'technology',
        'travel',
        'textures-patterns',
        'business-work',
        'animals',
        'food-drink',
        'athletics',
        'arts-culture',
        'history'
        ]
        os.mkdir(r'C:\Users\prath\OneDrive\Desktop\DatasetPreparation\images')
        for category in categories:
            path = os.path.join(r'C:\Users\prath\OneDrive\Desktop\DatasetPreparation\images',category)
            os.mkdir(path)

        self.category_data = []
        self.pos = 0
        self.count = 0
        self.start_urls = []
        
        for category in categories:
            for page in range(1,501):
                self.start_urls.append(
                    "https://unsplash.com/napi/topics/{}/photos?page={}&per_page=1"
                    .format(
                        category,
                        page
                    )
                )
                self.category_data.append(category)
    

    

    def parse(self, response):
        image = response.json()[0]
        item = imageItem()
        item['category'] = self.category_data[self.pos]
        self.pos+=1
        item['url'] = image['urls']['thumb']
        item['name'] = str(self.count)
        self.count+=1
        yield item
