package com.ejercicio.ms_caceres_anculle.clients;

import com.ejercicio.ms_caceres_anculle.response.ResponseSunat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "cliente-sunat", url = "https://api.apis.net.pe/v2/sunat/")
public interface ClienteSunat {
    @GetMapping("/ruc")
    ResponseSunat getInfoReniec(@RequestParam("numero") String numero,
                                @RequestHeader("Authorization") String authorization);
}
