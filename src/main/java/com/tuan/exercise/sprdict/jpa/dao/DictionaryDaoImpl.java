package com.tuan.exercise.sprdict.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.tuan.exercise.sprdict.jpa.entity.TransTypeDetail;
import com.tuan.exercise.sprdict.jpa.entity.Word;

@Repository
@Primary
public class DictionaryDaoImpl implements DictionaryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Word> getWordsRelative(String relativeKey, int transType, int rowStart, int rowCount) {
        String queryStr = "from Word as w "
                + "where w.key like :relativekey "
                + "and w.type = :transType "
                + "order by w.key";
        TypedQuery<Word> query = em.createQuery(queryStr, Word.class)
                .setParameter("relativekey", String.format("%%%s%%", relativeKey))
                .setParameter("transType", transType);

        if (rowStart > 0) {
            query.setFirstResult(rowStart - 1);
            query.setMaxResults(rowCount);
        }

        return query.getResultList();
    }

    @Override
    @Transactional
    public Word getWordById(int id) {
        String queryStr = "from Word as w "
                + "where w.id = :id";
        TypedQuery<Word> query = em.createQuery(queryStr, Word.class)
                .setParameter("id", id);

        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<TransTypeDetail> getTransTypes() {
        TypedQuery<TransTypeDetail> query = em.createQuery("from TransTypeDetail", TransTypeDetail.class);

        return query.getResultList();
    }

    @Override
    @Transactional
    public void saveMeanings(int wordId, String meanings) {
        String queryStr = "update Word as w set w.meanings = :meanings "
                + "where id = :id";

        Query query = em.createQuery(queryStr)
        .setParameter("meanings", meanings)
        .setParameter("id", wordId);

        query.executeUpdate();
    }

    @Override
    public void saveWord(Word word) {
        em.persist(word);
    }

    @Override
    @Transactional
    public void deleteWord(int wordId) {
        String queryStr = "delete from Word as w where w.id = :wordId";
        Query query = em.createQuery(queryStr)
                .setParameter("wordId", wordId);

        query.executeUpdate();
    }

    @Override
    @Transactional
    public Long getWordCount(String relativeKey, int transType) {
        String queryStr = "select count(*) as row_num "
                + "from Word as w "
                + "where w.key like :relativeKey "
                + "and w.type = :transType";
        TypedQuery<Long> query = em.createQuery(queryStr, Long.class)
                .setParameter("relativeKey", String.format("%%%s%%", relativeKey))
                .setParameter("transType", transType);

        return query.getSingleResult();
    }

}
