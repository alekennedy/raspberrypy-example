/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domotica.app.api;


import com.domotica.app.RaspberryDomoticaApplication;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ale
 */
@RestController
@CrossOrigin(origins = {"*"})
public class RestApi {
    
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "open-door", method = RequestMethod.GET)
    public ResponseEntity<?> addPresupuesto(){
       try {
            final GpioController gpio = GpioFactory.getInstance();
            GpioPinDigitalOutput rele = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "RELE-PUERTA", PinState.HIGH);
            rele.setShutdownOptions(Boolean.TRUE, PinState.HIGH);
            rele.setState(PinState.LOW);
            Logger.getLogger("-->").log(Level.INFO, "ABRIENDO PUERTAS: "+rele.isLow());
            Thread.sleep(3000);
            rele.setState(PinState.HIGH);
            Logger.getLogger("-->").log(Level.INFO, "CERRANDO PUERTAS: "+rele.isHigh());
            
            gpio.shutdown();
            gpio.unprovisionPin(rele);
            String json = "{\"opened\":"+true+"}";
            
            return new ResponseEntity<>(json, HttpStatus.ACCEPTED);    
       }catch(Exception ex){
           Logger.getLogger("-->").log(Level.SEVERE, "ERROR EN EL API OPEN-DOOR", ex);
            String json = "{\"opened\":"+false+"}";
            return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    } 
}
