class ShippingInfo:
    def __init__(self, distance, weight):
        self.distance = distance 
        self.weight = weight      


def pochtaru(info : ShippingInfo):
    base = 120
    rate = 30 if info.distance < 600 else 50
    return (base + (info.weight * rate)) * 1.01


def cdek(info: ShippingInfo):
    if info.weight <= 0.5: base = 350
    elif info.weight <= 2.0: base = 600
    elif info.weight <= 5.0: base = 950
    else: base = 1500
    multiplier = 1.0 if info.distance < 1000 else info.distance / 1000
    return (base * multiplier) * 1.008

def yandex(info : ShippingInfo):
    base = 150
    km_rate = 25
    w = (info.weight * info.weight / 50)  
    return (base + (info.distance * km_rate)) + w

class ShippingCalculator:
    def calculate_price(self, info: ShippingInfo):
        poc = pochtaru(info)
        cde = cdek(info)
        yan = yandex(info)
        
        optimal = min(poc, cde, yan)

        if optimal == poc: return CompanyInfo("pochta.ru", float(poc))
        if optimal == cde: return CompanyInfo("cdek.ru", float(cde))
        if optimal == yan: return CompanyInfo("yandex.ru", float(yan))
        
        return ("nil", float('inf'))
    

class CompanyInfo:
    def __init__(self, name_or_obj, price: float = None):
        if isinstance(name_or_obj, CompanyInfo):
            self._name = name_or_obj.get_name()
            self._price = name_or_obj.get_price()
        else:
            self._name = name_or_obj
            self._price = price

    def get_name(self): return self._name
    def get_price(self): return self._price