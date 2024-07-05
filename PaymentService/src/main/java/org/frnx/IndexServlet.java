package org.frnx;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<title>Payment Service</title>" +
                        "<script>" +
                        "function sendPayment() {" +
                        "const bankCode = document.getElementById('bankCode').value;" +
                        "const currency = document.getElementById('currency').value;" +
                        "const value1 = document.getElementById('value1').value;" +
                        "const docN = document.getElementById('docN').value;" +
                        "const docD = document.getElementById('docD').value;" +
                        "const budgetIncome = document.getElementById('budgetIncome').value;" +
                        "const budgetAccount = document.getElementById('budgetAccount').value;" +
                        "const xmlPayload = `" +
                        "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:smp='http://smp.ws.uz/'>" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<smp:sendPayment>" +
                        "<bank>" +
                        "<code>${bankCode}</code>" +
                        "</bank>" +
                        "<payment>" +
                        "<amount>" +
                        "<currency>${currency}</currency>" +
                        "<value>${value1}</value>" +
                        "</amount>" +
                        "<document>" +
                        "<docN>${docN}</docN>" +
                        "<docD>${docD}</docD>" +
                        "</document>" +
                        "<budget>" +
                        "<budgetIncome>${budgetIncome}</budgetIncome>" +
                        "<budgetAccount>${budgetAccount}</budgetAccount>" +
                        "</budget>" +
                        "</payment>" +
                        "</smp:sendPayment>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>`;" +
                        "const xhr = new XMLHttpRequest();" +
                        "xhr.open('POST', 'http://localhost:8029/MyService-1.0-SNAPSHOT/PaymentService', true);" +
                        "xhr.setRequestHeader('Content-Type', 'text/xml');" +
                        "xhr.onreadystatechange = function () {" +
                        "if (xhr.readyState === 4) {" +
                        "if (xhr.status === 200) {" +
                        "alert(`<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<soap:Envelope>\n" +
                        "<soap:Body>\n" +
                        "<sendPaymentResult xmlns=\"http://smp.ws.uz/\">\n" +
                        "<m:GetResponse>\n" +
                        "<m:MSG>OK</m:MSG>\n" +
                        "</m:GetResponse>\n" +
                        "</sendPaymentResult>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>\n`);" +
                        "} else {" +
                        "alert(`<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "${xhr.responseText}`);"+
                        "}" +
                        "}" +
                        "};" +
                        "xhr.send(xmlPayload);" +
                        "}" +
                        "</script>" +
                        "</head>" +
                        "<body>" +
                        "<h1>Payment Service</h1>" +
                        "<form onsubmit='event.preventDefault(); sendPayment();'>" +
                        "<label for='bankCode'>Bank Code:</label>" +
                        "<input type='text' id='bankCode' name='bankCode'><br><br>" +
                        "<label for='currency'>Currency:</label>" +
                        "<input type='text' id='currency' name='currency'><br><br>" +
                        "<label for='value1'>Value:</label>" +
                        "<input type='text' id='value1' name='value1'><br><br>" +
                        "<label for='docN'>Document Number (DocN):</label>" +
                        "<input type='text' id='docN' name='docN'><br><br>" +
                        "<label for='docD'>Document Date (DocD):</label>" +
                        "<input type='text' id='docD' name='docD'><br><br>" +
                        "<label for='budgetIncome'>Budget Income:</label>" +
                        "<input type='text' id='budgetIncome' name='budgetIncome'><br><br>" +
                        "<label for='budgetAccount'>Budget Account:</label>" +
                        "<input type='text' id='budgetAccount' name='budgetAccount'><br><br>" +
                        "<button type='submit'>Send Payment</button>" +
                        "</form>" +
                        "</body>" +
                        "</html>"
        );
    }
}