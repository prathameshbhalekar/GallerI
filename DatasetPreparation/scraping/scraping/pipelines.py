# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter
import urllib.request
import os


class ImagePipeline:
    def process_item(self, item, spider):
        category = item['category']
        url = item['url']
        name = item['name']

        path = os.path.join(
            r'C:\Users\prath\OneDrive\Desktop\DatasetPreparation\images',
            category,
            "{}.jpg".format(name)
            )

        urllib.request.urlretrieve(
            url,
            path
        )

        return item
