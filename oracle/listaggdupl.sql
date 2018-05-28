DROP TYPE LISTAGGDUPL_T;

CREATE OR REPLACE TYPE LISTOFLINES IS TABLE OF VARCHAR(100);

/

CREATE OR REPLACE EDITIONABLE TYPE LISTAGGDUPL_T AS OBJECT
(
  gDelimiter          VARCHAR2(1),
  gCollect            LISTOFLINES,

  STATIC FUNCTION ODCIAggregateInitialize(sctx  IN OUT  LISTAGGDUPL_T) RETURN NUMBER,

  MEMBER FUNCTION ODCIAggregateIterate(self   IN OUT  LISTAGGDUPL_T,
                                       value  IN      VARCHAR2 ) RETURN NUMBER,

  MEMBER FUNCTION ODCIAggregateTerminate(self         IN   LISTAGGDUPL_T,
                                         returnValue  OUT  VARCHAR2,
                                         flags        IN   NUMBER)
    RETURN NUMBER,

  MEMBER FUNCTION ODCIAggregateMerge(self  IN OUT  LISTAGGDUPL_T,
                                     ctx2  IN      LISTAGGDUPL_T)
    RETURN NUMBER
);
/
CREATE OR REPLACE EDITIONABLE TYPE BODY "LISTAGGDUPL_T" 
IS
  STATIC FUNCTION ODCIAggregateInitialize(sctx  IN OUT  LISTAGGDUPL_T)
    RETURN NUMBER IS
  BEGIN
   sctx := LISTAGGDUPL_T(',',LISTOFLINES());
   RETURN ODCIConst.Success;
  END;

  MEMBER FUNCTION ODCIAggregateIterate(self   IN OUT  LISTAGGDUPL_T,
                                       value  IN      VARCHAR2 )
    RETURN NUMBER IS
  BEGIN

   -- Already exists?
   self.gCollect.extend();
   self.gCollect(self.gCollect.LAST) := value;
   RETURN ODCIConst.Success;
  END;

  MEMBER FUNCTION ODCIAggregateTerminate(self         IN   LISTAGGDUPL_T,
                                         returnValue  OUT  VARCHAR2,
                                         flags        IN   NUMBER)
    RETURN NUMBER IS
  BEGIN

    returnValue := NULL;
    FOR Result IN ( SELECT DISTINCT O.COLUMN_VALUE FROM TABLE(self.gCollect) O ORDER BY 1 )
    LOOP
     returnValue := returnValue || CASE WHEN NOT returnValue IS NULL THEN self.gDelimiter END || Result.COLUMN_VALUE; 
    END LOOP;
    
    IF (returnValue IS NULL) THEN returnValue := ''; END IF;
      
    RETURN ODCIConst.Success;
  END;

  MEMBER FUNCTION ODCIAggregateMerge(self  IN OUT  LISTAGGDUPL_T,
                                     ctx2  IN      LISTAGGDUPL_T) RETURN NUMBER
  IS
  BEGIN

     FOR i IN ctx2.gCollect.FIRST..ctx2.gCollect.LAST 
     LOOP
       self.gCollect.extend();
       self.gCollect(self.gCollect.LAST) := ctx2.gCollect(i);
     END LOOP;  

    RETURN ODCIConst.Success;
  END;
END;
/
  
CREATE OR REPLACE EDITIONABLE FUNCTION LISTAGGDUPL (p_input VARCHAR2)
RETURN VARCHAR2
PARALLEL_ENABLE AGGREGATE USING LISTAGGDUPL_T;

