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
            cp, cn = strategy.implement_strategies(strategy, info)
            
            if cp < price:
                price = cp
                name = cn
                
        return round(price, 2), name