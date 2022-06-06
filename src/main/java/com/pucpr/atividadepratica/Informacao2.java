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

public class Informacao2 {
    
    public static class MapperInformacao2 extends Mapper<Object, Text, Text, IntWritable> {
        
        public void map(Object chave, Text valor, Context context) throws IOException, InterruptedException {
            String linha = valor.toString();
            String[] campos = linha.split(";");
            
            if((campos.length == 10) && (campos[0].equals("Brazil"))) {
                String mercadoria = campos[3];
                int quantidade = 1;
                
//                for(int i = 0; i < ; i++){
//                    
//                }
                
                Text chaveMap = new Text(mercadoria);
                IntWritable valorMap = new IntWritable(quantidade);
                
                context.write(chaveMap, valorMap);
 
            }
        }
        
    }
    
    public static class ReducerInformacao2 extends Reducer<Text, IntWritable, Text, IntWritable> {
    
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
        String arquivoSaida = "/home2/ead2022/SEM1/ramon.duarte/Desktop/atividadePratica/informacao2";
        
        if(args.length == 2){
            arquivoEntrada = args[0];
            arquivoSaida = args[1];
        }
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "implementacao2");
        
        job.setJarByClass(Informacao2.class);
        job.setMapperClass(MapperInformacao2.class);
        job.setReducerClass(ReducerInformacao2.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(arquivoEntrada));
        FileOutputFormat.setOutputPath(job, new Path(arquivoSaida));
        
        job.waitForCompletion(true);
 
    }
}
