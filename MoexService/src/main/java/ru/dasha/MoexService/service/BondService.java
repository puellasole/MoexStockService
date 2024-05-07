package ru.dasha.MoexService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ru.dasha.MoexService.dto.BondDto;
import ru.dasha.MoexService.dto.StocksDto;
import ru.dasha.MoexService.dto.TickersDto;
import ru.dasha.MoexService.exception.LimitRequestsException;
import ru.dasha.MoexService.model.Currency;
import ru.dasha.MoexService.model.Stock;
import ru.dasha.MoexService.moexclient.CorporateBondsClient;
import ru.dasha.MoexService.moexclient.GovBondsClient;
import ru.dasha.MoexService.parser.Parser;

@Service
public class BondService {
	private final CorporateBondsClient corporateBondsClient;
	private final GovBondsClient govBondsClient;
	private final Parser parser;
	
	public BondService(CorporateBondsClient corporateBondsClient, GovBondsClient govBondsClient, Parser parser) {
        this.corporateBondsClient = corporateBondsClient;
        this.govBondsClient = govBondsClient;
        this.parser = parser;
    }
	
	public StocksDto getBondsFromMoex(TickersDto tickersDto) {
		List<BondDto> allBonds = new ArrayList<>();
		allBonds.addAll(getCorporateBonds());
		allBonds.addAll(getGovBonds());
		
		List<BondDto> resultBonds = allBonds.stream()
				.filter(b -> tickersDto.getTickers().contains(b.getTicker()))
				.collect(Collectors.toList());
		
		List<Stock> stocks =  resultBonds.stream().map(b -> {
            return Stock.builder()
                    .ticker(b.getTicker())
                    .name(b.getName())
                    .figi(b.getTicker())
                    .type("Bond")
                    .currency(Currency.RUB)
                    .source("MOEX")
                    .build();
        }).collect(Collectors.toList());
		
        return new StocksDto(stocks);
	}
	
	public List<BondDto> getCorporateBonds(){
		String xmlFromMoex = corporateBondsClient.getBondsFromMoex();
		List<BondDto> bondDtos = parser.parse(xmlFromMoex);
		if(bondDtos.isEmpty()) {
			throw new LimitRequestsException("no response for corp bonds");
		}
		return bondDtos;
	}
	
	public List<BondDto> getGovBonds(){
		String xmlFromMoex = govBondsClient.getBondsFromMoex();
		List<BondDto> bonds = parser.parse(xmlFromMoex);
		if(bonds.isEmpty()) {
			throw new LimitRequestsException("no response for gov bonds");
		}
		return bonds;
	}

}
