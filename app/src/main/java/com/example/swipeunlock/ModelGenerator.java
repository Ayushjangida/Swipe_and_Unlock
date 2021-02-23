package com.example.swipeunlock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class ModelGenerator {


    BufferedReader datafile = readDataFile("ads.txt");
    Instances data;

    {
        try {
            data = new Instances(datafile);
            data.setClassIndex(data.numAttributes() - 1);

            Instance first = data.instance(0);
            Instance second = data.instance(1);
            data.delete(0);
            data.delete(1);

            Classifier ibk = new IBk();
            ibk.buildClassifier(data);

            double class1 = ibk.classifyInstance(first);
            double class2 = ibk.classifyInstance(second);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedReader readDataFile(String s) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

}

