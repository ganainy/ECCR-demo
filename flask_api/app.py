from flask import Flask, request, jsonify , send_file
from flask_restful import Api, Resource
import os

app = Flask(__name__)
api = Api(app)

 #end point for home page
class Home(Resource):
    def get(self):
        return {"/": "Server is up and running"}
api.add_resource(Home, "/")


###################################################################
 #end point to pass device identifiers and receive a targeted ad
class DeviceIdentifiers(Resource):
    def get(self):
        return {"data": "todo insert mock data received through the get request"}
    
    def post(self):
        # Extract the JSON data from the request
        data = request.get_json()
        if data:
            # If data is provided, return it in the response
            
            return {
    "id": "ad_67890",
    "title": "Innovative New Gadget!",
    "description": "Explore the amazing features of our innovative gadget and how it can revolutionize your daily life.",
    "ad_link": "https://drive.usercontent.google.com/download?id=10MloNfPUer1WiU1ruxljC8FSe_leo3aQ&export=view&authuser=0",
    "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
    "link": "https://example.com/products/innovative-gadget",
    "cta": "Find Out More",
    "adType": "banner",
    "displayDuration": 45,
    "impressionTrackingUrl": "https://example.com/tracking/impression?ad_id=ad_67890",
    "clickTrackingUrl": "https://example.com/tracking/click?ad_id=ad_67890",
    "created_at": "2024-07-01T10:00:00Z",
    "updated_at": "2024-07-01T10:00:00Z",
    "requestId": "req_12345",
    "sdkVersion": "3.0.0",
    "responseTime": 200,
    "currency": "EUR",
    "amount": 0.10,
    "message": "Advertisement retrieved successfully"
    }, 200
        else:
            # If no data is provided, return an error response
            return {"error": "No data provided"}, 400
        
api.add_resource(DeviceIdentifiers, "/deviceIdentifiers")



###################################################################
 #end point to receive a random ad since user declined the privacy policy
class RandomAd(Resource):
    def get(self):
        return {"data": "todo insert mock data received through the get request"}
    
    def post(self):
        # Extract the JSON data from the request
        data = request.get_json()
        if data:
            # If data is provided, return it in the response
            return {
                "id": "ad_12345",
        "title": "Exciting New Product!",
        "description": "Discover the features of our latest product and how it can benefit you.",
        "ad_link": "https://drive.usercontent.google.com/download?id=1saGUtm_e-SY-w2DXZvn_NYhBBYK1TXKp&export=view&authuser=0",
        "link": "https://example.com/products/new-product",
        "cta": "Learn More",
        "adType": "banner",
        "displayDuration": 30,
        "impressionTrackingUrl": "https://example.com/tracking/impression?ad_id=ad_12345",
        "clickTrackingUrl": "https://example.com/tracking/click?ad_id=ad_12345",
        "created_at": "2024-06-12T14:30:00Z",
        "updated_at": "2024-06-12T14:30:00Z",
	"requestId": "req_67890",
        "sdkVersion": "2.5.1",
        "responseTime": 150,
        "currency": "USD",
        "amount": 0.05,
	"message": "Ad successfully retrieved"}, 200
        else:
            # If no data is provided, return an error response
            return {"error": "No data provided"}, 400
        
api.add_resource(RandomAd, "/randomAd")


###################################################################
 #end point to receive a location targeted ad 
class LocalAd(Resource):
    def get(self):
        return {"data": "todo insert mock data received through the get request"}
    
    def post(self):
        # Extract the JSON data from the request
        data = request.get_json()
        if data:
            # If data is provided, return it in the response

             return {
  "id": "ad_123456",
  "title": "Neues Produkt!",
  "description": "Entdecken Sie die Funktionen unseres neuesten Produkts und wie es Ihnen zugute kommen kann.",
  "ad_link": "https://drive.usercontent.google.com/download?id=1tiYUyfFmaAj9xnwbQnwM1RpPuRz-kbR5&export=view&authuser=0",
  "link": "https://example.com/products/new-product",
  "cta": "Mehr erfahren",
  "adType": "Banner",
  "displayDuration": 30,
  "impressionTrackingUrl": "https://example.com/tracking/impression?ad_id=ad_12345",
  "clickTrackingUrl": "https://example.com/tracking/click?ad_id=ad_12345",
  "created_at": "2024-06-12T14:30:00Z",
  "updated_at": "2024-06-12T14:30:00Z",
  "requestId": "req_67890",
  "sdkVersion": "2.5.1",
  "responseTime": 200,
  "currency": "EUR",
  "amount": 0.05,
  "message": "Anzeige erfolgreich abgerufen"
}, 200
        else:
            # If no data is provided, return an error response
            return {"error": "No data provided"}, 400
        
api.add_resource(LocalAd, "/localAd")
###################################################################
 #end point to receive ip and mac and return a location targeted ad 
class LocalAdSideChannel(Resource):

    def get(self):
        return {"data": "todo insert mock data received through the get request"}
    
    def post(self):
        # Extract the JSON data from the request
        data = request.get_json()
        mac_address_md5 = data['m_id']
        ip_address_md5 = data['i_id']
        if data:
            # If data is provided, return it in the response

             return {
                "id": "ad_123456",
                "title": "Top Angebot!",
                "description": "Sparen Sie jetzt mit unserer Aktion!",
                "ad_link": "https://drive.usercontent.google.com/download?id=1tiYUyfFmaAj9xnwbQnwM1RpPuRz-kbR5&export=view&authuser=0",
                "latitude":"40.7128",
                "longitude":"-74.0060",
                "link": "https://example.com/products/new-product",
                "cta": "Mehr erfahren",
                "adType": "Banner",
                "displayDuration": 30,
                "impressionTrackingUrl": "https://example.com/tracking/impression?ad_id=ad_12345",
                "clickTrackingUrl": "https://example.com/tracking/click?ad_id=ad_12345",
                "created_at": "2024-06-12T14:30:00Z",
                "updated_at": "2024-06-12T14:30:00Z",
                "requestId": "req_67890",
                "sdkVersion": "2.5.1",
                "responseTime": 200,
                "currency": "EUR",
                "amount": 0.05,
                "message": "Anzeige erfolgreich abgerufen"
                }, 200
        else:
            # If no data is provided, return an error response
            return {"error": "No data provided"}, 400
        
api.add_resource(LocalAdSideChannel, "/localAdSideChannel")
###################################################################
#load code dynamically
@app.route('/download_ip_jar', methods=['GET'])
def download_ip_jar():
    file_path = os.path.join(app.root_path, 'static', 'ip.jar')
    return send_file(file_path, as_attachment=True)

@app.route('/download_mac_jar', methods=['GET'])
def download_mac_jar():
    file_path = os.path.join(app.root_path, 'static', 'mac.jar')
    return send_file(file_path, as_attachment=True)

###################################################################
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
