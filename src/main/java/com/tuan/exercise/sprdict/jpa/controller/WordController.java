package com.tuan.exercise.sprdict.jpa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuan.exercise.sprdict.jpa.constant.Web;
import com.tuan.exercise.sprdict.jpa.dao.DictionaryDao;
import com.tuan.exercise.sprdict.jpa.entity.TransTypeDetail;
import com.tuan.exercise.sprdict.jpa.entity.Word;
import com.tuan.exercise.sprdict.jpa.util.CommonUtil;

@Controller
public class WordController {

    @Value("${paging.item.count}")
    public int wordItemCount;

    @Value("${paging.pagination.item.count}")
    public int navigationItemCount;

    @Autowired
    private DictionaryDao dictionaryDao;

    @GetMapping(value = { "/", "/search" })
    public String search(
            @RequestParam(name = "search-word", defaultValue = "") String searchWord,
            @RequestParam(name = "trans-type", defaultValue = "0") int transType,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        int wordCount = wordItemCount;
        int totalPageCount = CommonUtil.getTotalPageCount(dictionaryDao.getWordCount(searchWord, transType), wordCount);
        if (page > totalPageCount) {
            page = totalPageCount;
        } else if (page < 1) {
            page = 1;
        }

        int rowStart = (page - 1) * wordCount + 1;
        model.addAttribute("words", dictionaryDao.getWordsRelative(searchWord, transType, rowStart, wordCount));

        int pageCount = navigationItemCount;
        if (pageCount > totalPageCount) {
            pageCount = totalPageCount;
        }
        int pageStart = CommonUtil.getPageStart(page, pageCount, totalPageCount);
        int pageEnd = CommonUtil.getPageEnd(pageStart, pageCount);

        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);

        model.addAttribute("searchWord", searchWord);
        model.addAttribute("transTypes", dictionaryDao.getTransTypes());
        model.addAttribute("curTransType", transType);
        model.addAttribute("curPage", page);
        return Web.INDEX;
    }

    @GetMapping(value = { "/edit", "/add" })
    public String getEditForm(
            @RequestParam(name = "word-id", defaultValue = "-1") int wordId,
            Model model) {

        Word word;
        boolean editMode = false;
        if (wordId >= 0) {
            word = dictionaryDao.getWordById(wordId);
            editMode = true;
        } else {
            word = new Word();
            editMode = false;
        }
        model.addAttribute("word", word);
        model.addAttribute("editMode", editMode);
        List<TransTypeDetail> transTypes = dictionaryDao.getTransTypes();
        model.addAttribute("transTypes", transTypes);

        return Web.WORD_INPUT;
    }

    @GetMapping("/delete")
    public String doDeleteWord(@RequestParam("word-id") int wordId) {
        dictionaryDao.deleteWord(wordId);

        return Web.Direct.getRedirect("/search", null);
    }

    @PostMapping("/edit")
    public String doEditWord(@ModelAttribute("word") Word updatedWord) {

        dictionaryDao.saveMeanings(updatedWord.getId(), updatedWord.getMeanings());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("search-word", updatedWord.getKey());
        paramMap.put("trans-type", updatedWord.getType());

        return Web.Direct.getRedirect("/search", paramMap);
    }

    @PostMapping("/add")
    public String doAddWord(@ModelAttribute("word") Word newWord) {

        dictionaryDao.saveWord(newWord);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("search-word", newWord.getKey());
        paramMap.put("trans-type", newWord.getType());

        return Web.Direct.getRedirect("/search", paramMap);
    }
}
