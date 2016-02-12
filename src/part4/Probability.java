package part4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Probability
{
    private String path;
    private Double[] probabilities;

    public Probability(String path)
    {
        this.path = path;
    }

    private Double[] read(String path)
    {
        if(path == null || path.isEmpty())
        {
            System.err.println("error: the path provided is null or empty.");
            System.exit(1);
        }

        BufferedReader reader = null;
        Double[] probabilities = null;
        try
        {
            reader = new BufferedReader(new FileReader(path));

            String line;
            Integer numberOfBalls;
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                try
                {
                    numberOfBalls = Integer.parseInt(line.trim());
                    if(numberOfBalls <= 0)
                    {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e)
                {
                    System.err.println("error: the first line of the file '" + path + "' is not structured properly" +
                            ".\n the first line should represent the number of balls.\n it must be positive number " +
                            "with the max value of '"+ Integer.MAX_VALUE + "'.");
                    throw new Exception();
                }
            }
            else
            {
                System.err.println("error: the first line of the file '" + path + "' is null or empty.");
                throw new Exception();
            }

            System.out.println("the number of balls = " + numberOfBalls);

            probabilities = new Double[numberOfBalls];
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                String[] tokens = line.trim().split("\\s+");
                if(tokens.length == numberOfBalls)
                {
                    try
                    {
                        Double sum = 0.0;
                        for (int counter = 0; counter < numberOfBalls; counter++)
                        {
                            Double number = Double.parseDouble(tokens[counter]);
                            if (number > 1.0 || number < 0.0)
                            {
                                throw new NumberFormatException();
                            }
                            probabilities[counter] = number;
                            sum += number;
                        }
                        if(sum != 1.0)
                        {
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        System.err.println("error: the second line of the file '" + path + "' is not structured " +
                                "properly.\n the last line should represent the probabilities of picking each ball.\n" +
                                " the values must be positive float numbers with the min value of 0.0 and the max " +
                                "value of 1.0.\n the sum of probabilities must equal to 1.0.");
                        throw new Exception();
                    }
                }
                else
                {
                    System.err.println("error: the second line of the file '" + path + "' doesn't provide a list of " +
                            "probabilities with the length of '" + numberOfBalls + "'.");
                    throw new Exception();
                }
            }
            else
            {
                System.err.println("error: the second line of the file '" + path + "' is null or empty.");
                throw new Exception();
            }

            System.out.println("the probabilities list:");
            for(Double probability : probabilities)
            {
                System.out.print(probability + " ");
            }
            System.out.println();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("error: the file '" + path + "' doesn't exist or is a directory.");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("error: an error occurred while reading from the file '" + path + "'.");
            System.exit(1);
        }
        catch (Exception e)
        {
            System.exit(1);
        }
        finally
        {
            try
            {
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e)
            {
                System.err.println("error: an error occurred while closing the file '" + path + "'.");
                System.exit(1);
            }
        }

        return probabilities;
    }

    private Integer pick()
    {
        Double random = new Random().nextDouble();
        Double cumulativeProbability = 0.0;
        for(int counter = 0; counter < probabilities.length; counter++)
        {
            cumulativeProbability += probabilities[counter];
            if(random <= cumulativeProbability)
            {
                return (counter + 1);
            }
        }
        return null;
    }

    public void compute()
    {
        this.probabilities = read(path);

        Integer[] numbers = {30, 300, 3000, 30000};
        for(Integer number : numbers)
        {
            int sum = 0;
            for(int counter = 0; counter < number; counter++)
            {
                sum += pick();
            }
            System.out.format("number of pickings = %-5d   E(x) = %f\n", number, (new Double(sum)/number));
        }
    }

    public static void main(String args[])
    {
        if(args.length == 1)
        {
            Probability probability = new Probability(args[0]);
            probability.compute();
        }
        else
        {
            System.err.println("error: the input is invalid. it should be the path of the input file.");
        }
    }
}