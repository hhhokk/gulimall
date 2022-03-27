package com.gulimall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zy
 * @create 2022-03-27-1:45
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

}
