package com.demonchou.agi.tool.third.login;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:applicationContext.xml" })
public class BaseTest
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseTest.class);

	@Before
	public void before()
	{
		log.info("开始测试");
	}

	@After
	public void after()
	{
		log.info("结束测试");
	}

	@Test
	public void testInit()
	{

	}
}
