package com.example.newMock.Controller;


import com.example.newMock.Model.ResponseDTO;
import com.example.newMock.Model.RequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                responseDTO.setCurrency("US");
                responseDTO.setBalance(random(2000));
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                responseDTO.setCurrency("EU");
                responseDTO.setBalance(random(1000));
            } else {
                maxLimit = new BigDecimal(10000);
                responseDTO.setCurrency("RUB");
                responseDTO.setBalance(random(10000));
            }

            String rqUUID = requestDTO.getRqUID();


            responseDTO.setRqUID(rqUUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setMaxLimit(maxLimit);

            log.error("****** Запрос ******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("****** Ответ ******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public static BigDecimal random(int range) {
        BigDecimal max = new BigDecimal(range);
        BigDecimal randFromDouble = new BigDecimal(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        actualRandomDec = actualRandomDec
                .setScale(2, BigDecimal.ROUND_DOWN);
        return actualRandomDec;
    }


}
