package com.lab.meet.Dao;

import com.lab.meet.Model.FileEntity;


public interface FileService {
    FileEntity storeFile(String type, String name, String path, String url);
}
