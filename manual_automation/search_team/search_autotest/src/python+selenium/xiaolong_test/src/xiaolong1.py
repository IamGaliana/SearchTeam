#coding = utf -8

class PersonInfo():
    def __init__(self,name,age):
        self.name = name
        self.age = age
    
    def set_name(self,name):
        self.name = name
    
    def get_name(self):
        return self.name
    
    def set_age(self,age):
        self.age = age
        
    def get_age(self):
        return self.age 