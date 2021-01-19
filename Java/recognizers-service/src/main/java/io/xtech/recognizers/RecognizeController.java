package io.xtech.recognizers;

import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.number.NumberRecognizer;
import com.microsoft.recognizers.text.numberwithunit.NumberWithUnitRecognizer;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.xtech.recognizers.models.RecognizeInput;

import java.util.List;

@Controller("/recognize")
public class RecognizeController {
    @Post(value = "/currency", produces = MediaType.APPLICATION_JSON)
    public List<ModelResult> recognizeCurrency(@Body RecognizeInput recognizeInput) {
        return NumberWithUnitRecognizer.recognizeCurrency(recognizeInput.getText(), recognizeInput.getCulture());
    }

    @Post(value = "/range", produces = MediaType.APPLICATION_JSON)
    public List<ModelResult> recognizeRange(@Body RecognizeInput recognizeInput) {
        return NumberRecognizer.recognizeNumberRange(recognizeInput.getText(), recognizeInput.getCulture());
    }
}
