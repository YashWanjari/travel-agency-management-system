class Package:
    def __init__(self, id, destination, price, duration, itinerary):
        self.id = id
        self.destination = destination
        self.price = price
        self.duration = duration
        self.itinerary = itinerary

    def display_summary(self):
        print(f"ID: {self.id} | {self.destination} | ₹{self.price} | Duration: {self.duration}")

    def display_details(self):
        print("\n-------------------------------------------")
        print(f"Package ID: {self.id}")
        print(f"Destination: {self.destination}")
        print(f"Price: ₹{self.price}")
        print(f"Duration: {self.duration}")
        print(f"Detailed Itinerary:\n{self.itinerary}")
        print("-------------------------------------------")