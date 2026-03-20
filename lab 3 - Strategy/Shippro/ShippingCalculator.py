from typing import List
import Strategies
from Strategies import ShippingInfo

class ShippingCalculator:
    def __init__(self, strategies: List[Strategies.Strategies]): 
        self._strategies = strategies

    def calculate_price(self, info: ShippingInfo):
        price = float('inf')
        name = "nil"
        
        for strategy in self._strategies:
            cp, cn = strategy.implement_strategies(info)
             
            if cp < price:
                price = cp
                name = cn
                
        return CompanyInfo(round(price, 2), name)
    
class CompanyInfo:
    def __init__(self, name, price: float):
        self._name = name
        self._price = price

    def get_name(self): return self._name
    def get_price(self): return self._price