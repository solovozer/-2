class ShippingInfo:
    def __init__(self, distance, weight):
        self.distance = distance 
        self.weight = weight      


def pochtaru(info : ShippingInfo):
    base = 150
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
    def calculate_price(self, info: ShippingInfo) -> tuple[str, float]:
        poc = pochtaru(info)
        cde = cdek(info)
        yan = yandex(info)
        
        optimal = min(poc, cde, yan)
        
        # Use simple parentheses to return a tuple
        if optimal == poc: return ("pochta.ru", float(poc))
        if optimal == cde: return ("cdek.ru", float(cde))
        if optimal == yan: return ("yandex.ru", float(yan))
        
        return ("unknown", 0.0)