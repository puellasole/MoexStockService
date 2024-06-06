package ru.dasha.MoexService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
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
	private static final Logger logger = LoggerFactory.getLogger(BondService.class);
	
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
	
	@Cacheable(value = "corps")
	public List<BondDto> getCorporateBonds(){
		logger.info("Getting corporate bonds from Moex");
		String xmlFromMoex = corporateBondsClient.getBondsFromMoex();
		List<BondDto> bondDtos = parser.parse(xmlFromMoex);
		if(bondDtos.isEmpty()) {
			logger.error("No response from moex - corp bonds");
			throw new LimitRequestsException("No response for corp bonds");
		}
		return bondDtos;
	}
	
	@Cacheable(value = "govs")
	public List<BondDto> getGovBonds(){
		logger.info("Getting gov bonds from Moex");
		String xmlFromMoex = govBondsClient.getBondsFromMoex();
		List<BondDto> bonds = parser.parse(xmlFromMoex);
		if(bonds.isEmpty()) {
			logger.error("No response from moex - gov bonds");
			throw new LimitRequestsException("No response for gov bonds");
		}
		return bonds;
	}

}
