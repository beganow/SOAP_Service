package org.frnx;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.soap.*;
import jakarta.xml.ws.soap.SOAPFaultException;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebService(targetNamespace = "http://smp.ws.uz/")
public class PaymentService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "HTTP GET method is not supported by this endpoint.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws SOAPFaultException,IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/xml;charset=UTF-8");
            Bank bank = new Bank();
            Payment payment = new Payment();
            String soapResponse = sendPayment(bank, payment);
            sendSOAPResponse(resp, soapResponse);
        } catch (SOAPFaultException e) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/xml;charset=UTF-8");
            List<String> errorMessages = new ArrayList<>();
            Iterator<jakarta.xml.soap.DetailEntry> iterator = e.getFault().getDetail().getDetailEntries();
            while (iterator.hasNext()) {
                errorMessages.add(iterator.next().getTextContent());
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @WebMethod
    public String sendPayment(
            @WebParam(name = "bank") Bank bank,
            @WebParam(name = "payment") Payment payment) throws SOAPFaultException {
        List<String> errorMessages = new ArrayList<>();
        List<String> errorMessagesDOP = new ArrayList<>();
        try {
            validatePayment(bank, payment, errorMessages, errorMessagesDOP);
        } catch (SOAPFaultException e) {
            if (e.getFault().getFaultString().equals("error")) {
                throwSOAPError(errorMessagesDOP);
            } else {
                throwSOAPFault(errorMessages);
            }
        }
        if (!errorMessages.isEmpty()) {
            throwSOAPFault(errorMessages);
        }
        if (!errorMessagesDOP.isEmpty()) {
            throwSOAPError(errorMessagesDOP);
        }
        /*MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();

        // Construct your SOAP response structure
        SOAPElement sendPaymentResult = body.addChildElement(envelope.createName("sendPaymentResult", "m", "http://smp.ws.uz/"));
        SOAPElement getResponse = sendPaymentResult.addChildElement("GetResponse", "m", "http://smp.ws.uz/");
        SOAPElement msg = getResponse.addChildElement("MSG", "m", "http://smp.ws.uz/");
        msg.addTextNode("OK");

        // Save changes to the message
        message.saveChanges();

        return message;*/
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Body>" +
                "    <sendPaymentResult xmlns=\"http://smp.ws.uz/\">" +
                "      <m:GetResponse xmlns:m=\"http://smp.ws.uz/\">" +
                "        <m:MSG>OK</m:MSG>" +
                "      </m:GetResponse>" +
                "    </sendPaymentResult>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
    }

    private void validatePayment(Bank bank, Payment payment, List<String> errorMessages, List<String> errorMessagesDOP) throws SOAPFaultException {
        /*if (!isNumeric(bank.getCode())) {
            errorMessagesDOP.add("bank_code is not a valid number");
        }*/
        if (bank.getCode() == null || bank.getCode().isEmpty()) {
            errorMessages.add("code is empty");
        }
        if (Double.parseDouble(payment.getBudget().getBudgetAccount()) < Double.parseDouble(payment.getAmount().getValue())) {
            errorMessagesDOP.add("budget_account is less than amount value");
        }
        /*if (!isNumeric(payment.getAmount().getCurrency())) {
            errorMessagesDOP.add("currency is not a valid number");
        }*/
        if (payment.getAmount().getCurrency() == null || payment.getAmount().getCurrency().isEmpty()) {
            errorMessages.add("currency is empty");
        }
        /*if (!isNumeric(payment.getAmount().getValue())) {
            errorMessagesDOP.add("value is not a valid number");
        }*/
        if (payment.getAmount().getValue() == null || payment.getAmount().getValue().isEmpty()) {
            errorMessages.add("value is empty");
        }
        /*if (!isNumeric(payment.getDocument().getDocN())){
            errorMessagesDOP.add("doc_n is not a valid number");
        }*/
        if (payment.getDocument().getDocN() == null || payment.getDocument().getDocN().isEmpty()) {
            errorMessages.add("doc_n is empty new");
        }
        /*if (!isNumeric(payment.getDocument().getDocD())){
            errorMessagesDOP.add("doc_d is not a valid number");
        }*/
        if (payment.getDocument().getDocD() == null || payment.getDocument().getDocD().isEmpty()) {
            errorMessages.add("doc_d is empty");
        }
        /*if (!isNumeric(payment.getBudget().getBudgetIncome())){
            errorMessagesDOP.add("budget_income is not a valid number");
        }*/
        if (payment.getBudget().getBudgetIncome() == null || payment.getBudget().getBudgetIncome().isEmpty()) {
            errorMessages.add("budget_income is empty");
        }
        /*if (!isNumeric(payment.getBudget().getBudgetAccount())){
            errorMessagesDOP.add("budget_account is not a valid number");
        }*/
        if (payment.getBudget().getBudgetAccount() == null || payment.getBudget().getBudgetAccount().isEmpty()) {
            errorMessages.add("budget_account is empty");
        }

        if (!errorMessages.isEmpty()) {
            throwSOAPFault(errorMessages);
        }
        if (!errorMessagesDOP.isEmpty()) {
            throwSOAPError(errorMessagesDOP);
        }
    }


    private void throwSOAPFault(List<String> messages) throws SOAPFaultException {
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString("is empty");
            fault.setFaultCode(new QName("1"));

            Detail detail = fault.addDetail();
            for (String message : messages) {
                detail.addDetailEntry(new QName("string")).addTextNode(message);
            }

            throw new SOAPFaultException(fault);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }
    private void throwSOAPError(List<String> messagesDOP) throws SOAPFaultException {
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString("error");
            fault.setFaultCode(new QName("1"));

            Detail detail = fault.addDetail();
            for (String message : messagesDOP) {
                detail.addDetailEntry(new QName("string")).addTextNode(message);
            }

            throw new SOAPFaultException(fault);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendSOAPResponse(HttpServletResponse resp, String soapMessage) throws IOException {
        try (OutputStream out = resp.getOutputStream()) {
            out.write(soapMessage.getBytes());
            out.flush();
        }
    }
    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
