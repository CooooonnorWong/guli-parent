package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private VideoMapper videoMapper;

    @Override
    public List<Chapter> getChaptersAndVideos(String courseId) {
        /*
            //Java代码实现
            LambdaQueryWrapper<Chapter> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Chapter::getCourseId, courseId);
            List<Chapter> chapters = baseMapper.selectList(queryWrapper);
            chapters.forEach(chapter -> {
            LambdaQueryWrapper<Video> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(Video::getChapterId, chapter.getId());
            List<Video> videos = videoMapper.selectList(queryWrapper2);
            chapter.setVideos(videos);
        });

         */
        return baseMapper.selectChaptersAndVideos(courseId);
    }

    @Override
    public boolean removeByIdAndCheckVideo(String id) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getChapterId, id);
        if (videoMapper.selectList(queryWrapper).size() > 0) {
            return false;
        }
        baseMapper.deleteById(id);
        return true;
    }
}
