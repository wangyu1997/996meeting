package com.lab.meet.Dao.Impl;

import com.lab.meet.Dao.FileService;
import com.lab.meet.Model.FileEntity;
import com.lab.meet.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Override
    public FileEntity storeFile(String type, String name, String path, String url) {
        FileEntity file = new FileEntity();
        file.setType(type);
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        fileRepository.save(file);
        return file;
    }
}
