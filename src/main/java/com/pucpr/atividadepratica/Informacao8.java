package com.pucpr.atividadepratica;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Informacao8 {
    
    public static class MapperInformacao8 extends Mapper<Object, Text, Text, IntWritable> {
        
        @Override
        public void map(Object chave, Text valor, Context context) throws IOException, InterruptedException {
            String linha = valor.toString();
            String[] campos = linha.split(";");
            
            if(campos.length == 10) {
                String pais = campos[0];
                int quantidade = 1;
                
//                for(int i = 0; i < ; i++){
//                    
//                }
                
                Text chaveMap = new Text(pais);
                IntWritable valorMap = new IntWritable(quantidade);
                
                context.write(chaveMap, valorMap);
 
            }
        }
        
    }
    
    public static class ReducerInformacao8 extends Reducer<Text, IntWritable, Text, IntWritable> {
    
            @Override
            public void reduce(Text chave, Iterable<IntWritable> valores, Context context) throws IOException, InterruptedException {
                int soma = 0;
                
                for(IntWritable valor : valores){
                    soma += valor.get();
                }
   
                context.write(chave, new IntWritable(soma));
            }
    }
    
    public static void main(String[] args) throws Exception {

        String arquivoEntrada = "/home/Disciplinas/FundamentosBigData/OperacoesComerciais/base_100_mil.csv";
        String arquivoSaida = "/home2/ead2022/SEM1/ramon.duarte/Desktop/atividadePratica/Informacao8";
        
        if(args.length == 2){
            arquivoEntrada = args[0];
            arquivoSaida = args[1];
        }
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "implementacao8");
        
        job.setJarByClass(Informacao8.class);
        job.setMapperClass(MapperInformacao8.class);
        job.setReducerClass(ReducerInformacao8.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(arquivoEntrada));
        FileOutputFormat.setOutputPath(job, new Path(arquivoSaida));
        
        job.waitForCompletion(true);
 
    }
}
