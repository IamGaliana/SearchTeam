#encoding:utf8

import time
import datetime

def date_formate_to_stamp_today():
    return time.mktime(datetime.date.today().timetuple())

def date_format_to_stamp1(year, month, date):
    dateC = datetime.datetime(year, month, date, 0, 0, 0)
    datestamp = time.mktime(dateC.timetuple())
    return datestamp

def date_format_to_stamp2(date_in):
    if '-' in date_in:
        element = date_in.split('-')
        print element
    elif '/' in date_in:
        element = date_in.split('/')
        print element
    year, month, date = element[0], element[1], element[2]
    dateC = datetime.datetime(int(year), int(month), int(date), 0, 0, 0)
    datestamp = time.mktime(dateC.timetuple())
    return datestamp
