package ru.dasha.MoexService.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.dasha.MoexService.dto.StocksDto;
import ru.dasha.MoexService.dto.TickersDto;
import ru.dasha.MoexService.service.BondService;

@RestController
@RequestMapping("/bonds")
public class BondMoexController {
    private final BondService bondService;

    public BondMoexController(BondService bondService) {
        this.bondService = bondService;
    }
    
    @PostMapping("/getBondsByTickers")
    public StocksDto getBondsFromMoex(@RequestBody TickersDto tickersDto) {
        return bondService.getBondsFromMoex(tickersDto);
    }
}