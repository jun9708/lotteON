package kr.co.lotteon.service;

import kr.co.lotteon.dto.CompanyStoryDTO;
import kr.co.lotteon.entity.CompanyStory;
import kr.co.lotteon.repository.CompanyStoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyStoryRepository companyStoryRepository;
    private final ModelMapper modelMapper;

    // company - story 게시글 조회
    public List<CompanyStoryDTO> selectCompanyStory(String storyCate){
        log.info("storyCate : " + storyCate);
        List<CompanyStory> companyStory = new ArrayList<>();
        if (storyCate == null) {
            companyStory = companyStoryRepository.findAll();
        }else {
            companyStory = companyStoryRepository.findByStoryCate(storyCate);
        }
        List<CompanyStoryDTO> companyStoryList = new ArrayList<>();
        for (CompanyStory story : companyStory) {
            CompanyStoryDTO companyStoryDTO = modelMapper.map(story, CompanyStoryDTO.class);
            companyStoryList.add(companyStoryDTO);
        }
        return companyStoryList;
    }
}
