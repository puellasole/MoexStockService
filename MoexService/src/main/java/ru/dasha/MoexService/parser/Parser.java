package ru.dasha.MoexService.parser;

import java.util.List;

import ru.dasha.MoexService.dto.BondDto;

public interface Parser {
	List<BondDto> parse(String ratesAsStrings);

}
