package org.openjfx.dpeng.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.openjfx.dpeng.database.JDBCUtil;
import org.openjfx.dpeng.database.model.Word;

public class DictionaryDAO implements DAOInterface<Word> {

    public static DictionaryDAO getInstance() {
        return new DictionaryDAO();
    }

    @Override
    public int insert(Word word) {
        int result = 0;
        try {
            Connection connection = JDBCUtil.getConnection();

            String SQL = "REPLACE INTO dictionary (word, textDescription, htmlDescription) "
                    + " VALUES (?, ?, ?)" ;
            PreparedStatement pStatement = connection.prepareStatement(SQL);

            pStatement.setString(1, word.getKey());
            pStatement.setString(2, word.getTextDescription());
            pStatement.setString(3, word.getHtmlDescription());

            result = pStatement.executeUpdate();

            System.out.println("Bạn đã thực thi: " + SQL);
            System.out.println("Có " + result + " dòng bị thay đổi!");

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int update(Word word) {
        int result = 0;
        try {
            Connection connection = JDBCUtil.getConnection();

            String SQL = "UPDATE dictionary " +
                    " SET " +
                    " textDescription = ? " +
                    ", htmlDescription = ? " +
                    " WHERE word = ? ";
            PreparedStatement pStatement = connection.prepareStatement(SQL);

            pStatement.setString(1, word.getTextDescription());
            pStatement.setString(2, word.getHtmlDescription());
            pStatement.setString(4, word.getKey());

            result = pStatement.executeUpdate();

            System.out.println("Bạn đã thực thi: " + SQL);
            System.out.println("Có " + result + " dòng bị thay đổi!");

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int delete(Word word) {
        int result = 0;
        try {
            Connection connection = JDBCUtil.getConnection();

            String SQL = "DELETE FROM dictionary " +
                    " WHERE word = ? ";
            PreparedStatement pStatement = connection.prepareStatement(SQL);
            pStatement.setString(1, word.getKey());
            
            result = pStatement.executeUpdate();

            System.out.println("Bạn đã thực thi: " + SQL);
            System.out.println("Có " + result + " dòng bị thay đổi!");

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Word> selectAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public Word selectById(Word word) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public ArrayList<Word> selectByCondition(String condition) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectByCondition'");
    }

    public ArrayList<String> suggestByKeyWord(String kword) {
        ArrayList<String> suggestions = new ArrayList<>();
        kword += "%";
        int count = 0;
        try {
            Connection connection = JDBCUtil.getConnection();

            String SQL = "SELECT word FROM dictionary " +
                    " WHERE word LIKE ? ";
            PreparedStatement pStatement = connection.prepareStatement(SQL);
            pStatement.setString(1, kword);
            
            ResultSet suggestionWords = pStatement.executeQuery();
            if (suggestionWords != null) {
                while (suggestionWords.next() && count < 70) {
                    String word = suggestionWords.getString("word");

                    suggestions.add(word);
                    count ++;
                }
            }

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suggestions;
    }

    public Word selectByKeyWord(String kword) {
        Word word = null;
        try {
            Connection connection = JDBCUtil.getConnection();

            String SQL = "SELECT * FROM dictionary " +
                    " WHERE word = ? ";
            PreparedStatement pStatement = connection.prepareStatement(SQL);
            pStatement.setString(1, kword);
            
            ResultSet suggestionWords = pStatement.executeQuery();
            if (suggestionWords != null) {
                while(suggestionWords.next()) {
                    String key = suggestionWords.getString("word");
                    String textDescription = suggestionWords.getString("textDescription");
                    String htmlDescription = suggestionWords.getString("htmlDescription");
    
                    word = new Word(key, textDescription, htmlDescription);
                    break;
                }
            }

            JDBCUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return word;
    }

}
