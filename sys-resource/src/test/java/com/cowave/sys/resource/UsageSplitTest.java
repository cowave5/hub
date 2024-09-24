/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource;

import java.util.Arrays;
import java.util.List;

import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.service.UsageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author shanhuiming
 */
@SpringBootTest
public class UsageSplitTest {

	@Autowired
	private UsageService usageService;

	/**
	 * 0-10
	 * [0-5] [5,10]
	 */
	@Test
	public void test1() {
		Usage usage = usage(0L, 10L);

		ResourcePool a1 = resourcePool(0L, 5L);
		ResourcePool a2 = resourcePool(5L, 10L);
		List<ResourcePool> list = Arrays.asList(a1, a2);

		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Usage usage1 = srcUsages.get(0);

		assertEquals(Long.valueOf(0), usage1.getFreqBegin());
		assertEquals(Long.valueOf(5), usage1.getFreqEnd());

		Usage usage2 = srcUsages.get(1);
		assertEquals(Long.valueOf(5), usage2.getFreqBegin());
		assertEquals(Long.valueOf(10), usage2.getFreqEnd());
	}

	/**
	 * 10-20
	 * [0-15] [13,25]
	 */
	@Test
	public void test2() {
		Usage usage = usage(10L, 20L);
		ResourcePool a1 = resourcePool(0L, 15L);
		ResourcePool a2 = resourcePool(13L, 25L);
		List<ResourcePool> list = Arrays.asList(a1, a2);

		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Usage usage1 = srcUsages.get(0);
		assertEquals(Long.valueOf(10), usage1.getFreqBegin());
		assertEquals(Long.valueOf(15), usage1.getFreqEnd());

		Usage usage2 = srcUsages.get(1);
		assertEquals(Long.valueOf(15), usage2.getFreqBegin());
		assertEquals(Long.valueOf(20), usage2.getFreqEnd());
	}

	/**
	 * 10-20
	 * [0-14] [15,25]
	 */
	@Test
	public void test3() {
		Usage usage = usage(10L, 20L);
		ResourcePool a1 = resourcePool(0L, 14L);
		ResourcePool a2 = resourcePool(15L, 25L);
		List<ResourcePool> list = Arrays.asList(a1, a2);

		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Assert.isTrue(srcUsages.isEmpty(), "error");
	}

	/**
	 * 10-20
	 * [0-14] [13,19]
	 */
	@Test
	public void test4() {
		Usage usage = usage(10L, 20L);
		ResourcePool a1 = resourcePool(0L, 14L);
		ResourcePool a2 = resourcePool(13L, 19L);
		List<ResourcePool> list = Arrays.asList(a1, a2);

		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Assert.isTrue(srcUsages.isEmpty(), "error");
	}

	/**
	 * 7-18
	 * [0-10] [2,15] [8,20]
	 */
	@Test
	public void test5() {
		Usage usage = usage(7L, 18L);
		ResourcePool a1 = resourcePool(0L, 10L);
		ResourcePool a2 = resourcePool(2L, 15L);
		ResourcePool a3 = resourcePool(8L, 20L);
		List<ResourcePool> list = Arrays.asList(a1, a2, a3);

		// 方案选取 7-10,10-18   7-15,15-18   7-10,10-15,15-18
		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Usage usage1 = srcUsages.get(0);
		assertEquals(Long.valueOf(7), usage1.getFreqBegin());
		assertEquals(Long.valueOf(10), usage1.getFreqEnd());

		Usage usage3 = srcUsages.get(1);
		assertEquals(Long.valueOf(10), usage3.getFreqBegin());
		assertEquals(Long.valueOf(18), usage3.getFreqEnd());
	}

	/**
	 * 3-28
	 * [0-10] [7,15] [9,30]
	 */
	@Test
	public void test6() {
		Usage usage = usage(3L, 28L);
		ResourcePool a1 = resourcePool(0L, 10L);
		ResourcePool a2 = resourcePool(7L, 15L);
		ResourcePool a3 = resourcePool(9L, 30L);
		List<ResourcePool> list = Arrays.asList(a1, a2, a3);

		List<Usage> srcUsages = usageService.usageSplit(list, usage);
		Usage usage1 = srcUsages.get(0);
		assertEquals(Long.valueOf(3), usage1.getFreqBegin());
		assertEquals(Long.valueOf(10), usage1.getFreqEnd());

		Usage usage3 = srcUsages.get(1);
		assertEquals(Long.valueOf(10), usage3.getFreqBegin());
		assertEquals(Long.valueOf(28), usage3.getFreqEnd());
	}

	private ResourcePool resourcePool(Long begin, Long end){
		ResourcePool pool = new ResourcePool();
		pool.setDownFreqBegin(begin);
		pool.setDownFreqEnd(end);
		return pool;
	}

	private Usage usage(Long begin, Long end){
		Usage usage = new Usage();
		usage.setFreqBegin(begin);
		usage.setFreqEnd(end);
		return usage;
	}
}
