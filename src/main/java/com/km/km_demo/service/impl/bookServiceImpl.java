package com.km.km_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.km.km_demo.dao.entity.book;
import com.km.km_demo.dao.mapper.bookMapper;
import com.km.km_demo.service.bookService;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/4 0004
 * @since 1.0.0
 */
@Service
public class bookServiceImpl extends ServiceImpl<bookMapper, book> implements bookService {
}
