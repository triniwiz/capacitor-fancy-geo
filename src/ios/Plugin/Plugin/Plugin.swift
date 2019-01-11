import Foundation
import Capacitor
import FancyGeo

typealias JSObject = [String:Any]
typealias JSArray = [Any]
@objc(Geo)
public class Geo: CAPPlugin {
    var fancyGeo: FancyGeo?

    public override func load() {
        FancyGeo.initFancyGeo()
    }
    @objc func setOnMessageListener(_ call: CAPPluginCall) {
        FancyGeo.onMessageReceivedListener = ({message in
            var object: JSObject = [:]
            let type = FancyGeo.getType(json: message)
            if(type.elementsEqual("circle")){
                let fence = FancyGeo.CircleFence.fromString(json: message)
                if(fence != nil){
                    object["transition"] = fence?.transition.rawValue
                    object["loiteringDelay"] = fence?.loiteringDelay
                    object["coordinates"] = fence?.coordinates
                    object["id"] = fence?.id
                    var notification: JSObject = [:]
                    if(fence?.notification != nil){
                        notification["body"] = fence?.notification?.body
                        notification["id"] = fence?.notification?.id
                        notification["title"] = fence?.notification?.title
                    }
                    object["notification"] = notification
                }
                call.success(object)
            }

        })
    }

    @objc func initiate(_ call: CAPPluginCall) {
        call.success()
    }

    @objc func createCircleFence(_ call: CAPPluginCall) {
        let id = call.getString("id")
        let transition = call.getInt("transition") ?? -1
        if (transition == -1) {
            call.reject("Transition missing");
            return;
        }
        let loiteringDelay = call.getInt("loiteringDelay") ?? 0
        let pointsArray = call.getArray("points", Double.self) ?? []
        var points:[Double] = []
        let lat = pointsArray[0];
        let lon = pointsArray[1];
        points[0] = lat
        points[1] = lon
        let radius = call.getDouble("radius")
        let notificationObj: JSObject? = call.getObject("notification")
        var notification: FancyGeo.FenceNotification? = nil
        if (notificationObj != nil) {
            notification = FancyGeo.FenceNotification.initWithIdTitleBody(id: notificationObj?["id"] as! Int, title: notificationObj?["title"] as! String, body: notificationObj?["body"] as! String)
        }


        fancyGeo?.createFenceCircle(id: id, transition: FancyGeo.FenceTransition.init(rawValue: transition)!, notification: notification, loiteringDelay: loiteringDelay, points: points, radius: radius!, onListener: { (success, error) in
            if(success != nil){
                var object: JSObject = [:]
                object["id"] = id
                call.resolve(object)
            }else{
                call.reject(error!)
            }
        })

    }

    @objc func hasPermission(_ call: CAPPluginCall) {
        let has = FancyGeo.hasPermission()
        var obj:JSObject = [:]
        obj["value"] = has
        call.resolve(obj)

    }

    @objc func requestPermission(_ call: CAPPluginCall) {
        let always = call.getBool("always") ?? false
        FancyGeo.requestPermission(always: always, callback: { (hasPermission, error ) in
            if(error != nil){
                call.reject(error!)
            }else{
                var object: JSObject = [:]
                object["value"] = hasPermission
            }
        })
    }

    @objc func getCurrentLocation(_ call: CAPPluginCall) {
        fancyGeo?.getCurrentLocation(listener: {

        })
    }
}
