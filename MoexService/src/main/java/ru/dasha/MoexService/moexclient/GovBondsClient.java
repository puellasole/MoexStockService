package ru.dasha.MoexService.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "govbonds", url = "${moex.bonds.government.url}")
public interface GovBondsClient {
	@GetMapping
	String getBondsFromMoex();
}
