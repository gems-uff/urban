from selenium import webdriver
import selenium
import time
import os
import urllib2

def find_elements():
	elements = browser.find_element_by_class_name(UL_LIST_CLASS).find_elements_by_class_name(LI_CLASS)
	for li in elements:
		li_btn = li.find_element_by_class_name(DIV_BUTTON_CLASS).find_element_by_class_name(UL_BUTTONS_CLASS).find_elements_by_tag_name('li')[LI_BTN_DOWNLOAD_INDEX]
		
		href = li_btn.find_element_by_tag_name('a').get_attribute('href')
		extension = href[href.rfind(".")+1:]
		if not extension == 'pdf': 
			data = urllib2.urlopen(href).read()
			filename = href[href.rfind("/")+1:]
			
			print filename
			with open(DIRECTORY + filename, "wb") as code:
				code.write(data)
	return	

URL = "http://data.rio.rj.gov.br/dataset/pontos-de-parada-de-onibus"

UL_LIST_CLASS = 'resource-list'

LI_CLASS = 'resource-item'

DIV_BUTTON_CLASS = 'dropdown'

UL_BUTTONS_CLASS = 'dropdown-menu'

LI_BTN_DOWNLOAD_INDEX = 1

DIRECTORY = os.getcwd() + "/csvs/"

try:

	if not os.path.exists(DIRECTORY):
 		os.makedirs(DIRECTORY)
	
	browser = webdriver.Firefox()
	browser.set_page_load_timeout(20)
	browser.get(URL)

	find_elements()
except selenium.common.exceptions.TimeoutException, e:
	find_elements()
else:
	pass
finally:
	print 'finished'

	browser.quit()

	