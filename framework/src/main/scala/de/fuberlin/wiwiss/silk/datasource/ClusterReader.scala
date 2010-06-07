package de.fuberlin.wiwiss.silk.datasource

import de.fuberlin.wiwiss.silk.Instance
import java.io.{ObjectInputStream, FileInputStream, File}

class ClusterReader(dir : File) extends IndexedSeq[List[Instance]]
{
    val length =
    {
        val partitionFiles = dir.list.map(name => name.dropWhile(!_.isDigit)).filter(!_.isEmpty)
        if(partitionFiles.isEmpty)
        {
            throw new IllegalArgumentException("No cluster files found in " + dir)
        }
        else
        {
            partitionFiles.map(_.toInt).max + 1
        }
    }

    def apply(index : Int) : List[Instance] =
    {
        val stream = new ObjectInputStream(new FileInputStream(dir + "/cluster" + index))

        try
        {
            val clusterSize = stream.readInt()
            var cluster = List[Instance]()

            for(i <- 0 until clusterSize)
            {
                cluster ::= stream.readObject().asInstanceOf[Instance]
            }

            cluster
        }
        finally
        {
            stream.close()
        }
    }
}
