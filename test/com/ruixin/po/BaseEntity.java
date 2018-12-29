package com.ruixin.po;

import com.ruixin.annotation.Bean;
import com.ruixin.annotation.Value;

@Bean
public class BaseEntity{

	@Value("bean.package")
	private String pak;
	
}
