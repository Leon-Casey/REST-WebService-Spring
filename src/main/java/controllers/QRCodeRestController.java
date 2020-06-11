/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BreweriesService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Breweries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

/**
 *
 * @author Leon
 */
@RestController
@RequestMapping("/code")
public class QRCodeRestController {

    @Autowired
    BreweriesService service;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCode(@PathVariable("id") int id) throws WriterException, IOException {
        Breweries brewery = service.getBreweryById(id);

        String url = "https://www.google.com/";
        if (brewery.getWebsite() != null && !brewery.getWebsite().isEmpty()) {
            url = brewery.getWebsite();
        }

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage code = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(code, "jpeg", baos);
        baos.flush();
        byte[] codeInBytes = baos.toByteArray();
        baos.close();
        
        return codeInBytes;
    }
}
