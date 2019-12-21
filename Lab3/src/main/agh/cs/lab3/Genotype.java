package agh.cs.lab3;
import java.util.Arrays;
import java.lang.Math;

public class Genotype {

    private int[] genotypes;
    private int genRange = 8;
    private int size = 32;

    public Genotype(int genNo, int size) {
        this.genRange = genRange;
        this.size = size;
        genotypes = new int[size];

        randomFill();
        repairGen();
    }

    public Genotype(Genotype first, Genotype second) {
        this(8, 32);
        int p = (int) (Math.random() * (size - 1));
        int q = (int) (Math.random() * (size - 1));
        while(p!=q){
            p = (int) (Math.random() * (size - 1));
        }
        for (int i = 0; i < size; i++) {
            if(i<=p) genotypes[i] = first.getGenes()[i];
            if((i>p) && (i<=q)) genotypes[i] = second.getGenes()[i];
            if((i>q) && (i<size)) genotypes[i] = first.getGenes()[i];
        }
        repairGen();
    }

    public int[] getGenes() { return genotypes; }

    private void randomFill() {
        for (int i = 0; i < size; i++) {
            genotypes[i] = (int) (Math.random() * (genRange));
        }
        Arrays.sort(genotypes);
    }

    private void repairGen() {
        boolean doRepair = true;
        while (doRepair) {
            doRepair = false;

            int[] existingGen = new int[genRange];

            for (int i = 0; i < genRange; i++) {
                existingGen[i] = 0;
            }
            for (int i = 0; i < size; i++) {
                existingGen[genotypes[i]]++;
            }
            for (int i = 0; i < genRange; i++) {
                if (existingGen[i]==0) {
                    doRepair = true;
                }
            }
            if (doRepair) {
                for (int i = 0; i < genRange; i++) {
                    if (existingGen[i]==0) {
                        genotypes[(int) (Math.random() * (size))] = i;
                    }
                }
            }
        }
        Arrays.sort(genotypes);
    }

    public int randomGen() { return genotypes[(int) (Math.random() * (size))]; }

}
